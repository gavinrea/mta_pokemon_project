// center the screen on the player, though you can modify it to do otherwise
int viewportX = player.returnX() - VIEWPORT_WIDTH/2;
int viewportY = player.returnY() - VIEWPORT_HEIGHT/2;

g.setColor(Color.WHITE);
g.fillRect(viewportX, viewportY, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);