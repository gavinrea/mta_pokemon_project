public class GameKeyController implements KeyEventDispatcher {

    private final int MAX_REPEAT_RATE = 100; // Hz

    private final LocalGame game;
    private final GamingContext context;
    private final Account account;
    Timer keyRepeatTimer;
    Map<Move, TimerTask> repeatingTasks = new EnumMap<Move, TimerTask>(Move.class);

    public GameKeyController(LocalGame game, GamingContext context,
            Account account) {
        this.game = game;
        this.context = context;
        this.account = account;
    }


    public boolean dispatchKeyEvent(KeyEvent e) {

        assert EventQueue.isDispatchThread();

        int kc = e.getKeyCode();

        if (e.getID() == KeyEvent.KEY_PRESSED) {

            // If repeat is activated, ignore KEY_PRESSED events.
            // Should actually not occur, since KEY_RELEASED *should* have been
            // intercepted since last KEY_PRESSED.
            if (kc == account.getInt(KC_MOVE_LEFT)  && !isRepeating(LEFT))      move(LEFT);
            if (kc == account.getInt(KC_MOVE_RIGHT) && !isRepeating(RIGHT))     move(RIGHT);
            if (kc == account.getInt(KC_SOFT_DROP)  && !isRepeating(SOFT_DROP)) move(SOFT_DROP);

            // Regular moves
            if (kc == account.getInt(KC_ROT_CW))        move(ROT_CW);
            if (kc == account.getInt(KC_ROT_CW2))       move(ROT_CW);
            if (kc == account.getInt(KC_ROT_CCW))       move(ROT_CCW);
            if (kc == account.getInt(KC_ROT_CCW2))      move(ROT_CCW);
            if (kc == account.getInt(KC_HARD_DROP))     move(HARD_DROP);
            if (kc == account.getInt(KC_SLIDE_DROP))    move(SLIDE_DROP);
            if (kc == account.getInt(KC_FULL_LEFT))     move(FULL_LEFT);
            if (kc == account.getInt(KC_FULL_RIGHT))    move(FULL_RIGHT);
            if (kc == account.getInt(KC_HOLD))          move(HOLD);

            if (kc == account.getInt(KC_SEND_TO_ME))    useSpecial(0);
            if (kc == account.getInt(KC_SEND_TO_1))     useSpecial(1);
            if (kc == account.getInt(KC_SEND_TO_2))     useSpecial(2);
            if (kc == account.getInt(KC_SEND_TO_3))     useSpecial(3);
            if (kc == account.getInt(KC_SEND_TO_4))     useSpecial(4);
            if (kc == account.getInt(KC_SEND_TO_5))     useSpecial(5);
            if (kc == account.getInt(KC_SEND_TO_6))     useSpecial(6);
            if (kc == account.getInt(KC_SEND_TO_7))     useSpecial(7);
            if (kc == account.getInt(KC_SEND_TO_8))     useSpecial(8);
            if (kc == account.getInt(KC_SEND_TO_9))     useSpecial(9);


            // Reported bug: Key repeat "lags on releases", that is, the key
            // continues to repeat a few ms after it has been released.
            // The following two lines gives one "upper" approximation of
            // when someone really wants to release the key.
            if (kc == account.getInt(KC_MOVE_RIGHT)) stopRepeating(LEFT);
            if (kc == account.getInt(KC_MOVE_LEFT)) stopRepeating(RIGHT);
        }


        if (e.getID() == KeyEvent.KEY_RELEASED) {
            if (kc == account.getInt(KC_MOVE_LEFT)) stopRepeating(LEFT);
            if (kc == account.getInt(KC_MOVE_RIGHT)) stopRepeating(RIGHT);
            if (kc == account.getInt(KC_SOFT_DROP)) stopRepeating(SOFT_DROP);
        }

        return false;
    }


    private synchronized void stopRepeating(Move m) {
        if (!isRepeating(m))
            return;
        repeatingTasks.get(m).cancel();
        repeatingTasks.remove(m);
    }


    private synchronized boolean isRepeating(Move m) {
        return repeatingTasks.get(m) != null;
    }


    private synchronized void move(Move move) {
        assert EventQueue.isDispatchThread();

        context.notIdleSinceStart();

        PlayfieldEvent pfe = game.move(move);

        // Fake wall kicks
        if ((move == ROT_CW || move == ROT_CCW) &&
                account.getBool(USE_FAKE_WALL_KICKS) && !pfe.pfChanged) {

            // Try RIGHT and ROT, then LEFT and ROT.
            Playfield pf = game.getPlayfield();
            if (pf.isFakeRotPossible(true, move == ROT_CW)) {
                game.move(RIGHT);
                game.move(move);
            } else if (pf.isFakeRotPossible(false, move == ROT_CW)) {
                game.move(LEFT);
                game.move(move);
            }
        }


        // Initiate key repeats
        int delay = account.getInt(KEY_REPEAT_DELAY);
        int rate = account.getInt(KEY_REPEAT_RATE);
        if (delay > 0 && rate > 0 && isRepeatable(move))
            startRepeating(move);
    }


    private boolean isRepeatable(Move m) {
        return m == LEFT || m == RIGHT || m == SOFT_DROP;
    }


    private synchronized void startRepeating(Move move) {
        assert EventQueue.isDispatchThread();

        if (isRepeating(move))
            return;

        long delay = account.getInt(KEY_REPEAT_DELAY);
        int rate = account.getInt(KEY_REPEAT_RATE);

        Move repeatMove = move;
        if (rate >= MAX_REPEAT_RATE) {
            rate = MAX_REPEAT_RATE;
            repeatMove = move == LEFT      ? FULL_LEFT
                       : move == RIGHT     ? FULL_RIGHT
                       : move == SOFT_DROP ? SLIDE_DROP
                       : null; // not a repeatable move!
        }

        long period = (long) (1000.0 / rate);

        if (move == SOFT_DROP)
            delay = period;

        final Move m = repeatMove;
        TimerTask tt = new TimerTask() {

            // Should only be executed by keyRepeatTimer thread.
            public void run() {

                // Remove the if-branch below and you get old school GB behavior
                // With the if-branch it's more TDS-ish.
                // TODO: Make this depend on an account-setting
                if (m == SOFT_DROP && game.getPlayfield().isTetOnSurface()) {
                    stopRepeating(SOFT_DROP);
                    return;
                }

                game.move(m);

                // Attempt to make it more responsive to key-releases.
                // Even if there are multiple this-tasks piled up (due to
                // "scheduleAtFixedRate") we don't want this thread to take
                // precedence over AWT thread.
                Thread.yield();
            }
        };
        repeatingTasks.put(move, tt);
        keyRepeatTimer.scheduleAtFixedRate(tt, delay, period);
    }


    public synchronized void init() {
        if (!isInited()) {
            keyRepeatTimer = new Timer("Key Repeat Timer");
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        }
    }


    public synchronized boolean isInited() {
        return keyRepeatTimer != null;
    }


    public synchronized void uninit() {
        if (isInited()) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);

            keyRepeatTimer.cancel();
            keyRepeatTimer = null;
        }
    }


    private void useSpecial(int target) {
        context.notIdleSinceStart();
        context.useSpecial(target);
    }

}
