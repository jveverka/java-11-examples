package itx.examples.sshd.tests;

import itx.ssh.server.commands.repl.CommandRenderer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CommandRendererTest {

    private final static char LEFT  = 1;
    private final static char RIGHT = 2;
    private final static char BCK = 3;
    private final static char DEL = 4;
    private final static char HOME = 5;
    private final static char END = 6;

    @Test
    public void testEmptyCommandRenderer() {
        CommandRenderer commandRenderer = new CommandRenderer();
        String command = commandRenderer.getCommand();
        Assert.assertNotNull(command);
        Assert.assertEquals(command, "");
    }

    @Test
    public void testCommandRendererSimple() {
        CommandRenderer commandRenderer = new CommandRenderer();
        commandRenderer.onCharInsert((byte)'a');
        commandRenderer.onCharInsert((byte)'b');
        commandRenderer.onCharInsert((byte)'c');
        String command = commandRenderer.getCommand();
        Assert.assertNotNull(command);
        Assert.assertEquals(command, "abc");
        commandRenderer.reset();
        command = commandRenderer.getCommand();
        Assert.assertNotNull(command);
        Assert.assertEquals(command, "");
        commandRenderer.onCharInsert((byte)'D');
        commandRenderer.onCharInsert((byte)'C');
        commandRenderer.onCharInsert((byte)'E');
        command = commandRenderer.getCommand();
        Assert.assertNotNull(command);
        Assert.assertEquals(command, "DCE");
    }


    @DataProvider(name = "testInputsProvider")
    public static Object[][] getDataForTestInputs() {
        return new Object[][] {
                { new char[] {}, "" , 0 , true },
                { new char[] { 'a', 'b', 'c'  }, "abc", 3 , true },
                { new char[] { 'a', 'b', 'c', BCK  }, "ab", 2 , true },
                { new char[] { 'a', 'b', 'c', BCK, BCK  }, "a", 1 , true },
                { new char[] { 'a', 'b', 'c', BCK, BCK, BCK  }, "", 0 , true },
                { new char[] { 'a', 'b', 'c', BCK, BCK, BCK, BCK  }, "", 0 , true},
                { new char[] { BCK, BCK, BCK, BCK  }, "", 0 , true },
                { new char[] { BCK, LEFT, RIGHT, BCK  }, "", 0 , true },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, RIGHT, RIGHT, LEFT }, "abcd", 3, false },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, BCK }, "acd", 1, false },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, BCK, BCK }, "cd", 0, false },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, BCK, BCK, BCK }, "cd", 0 , false },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, BCK, BCK, BCK, BCK }, "cd", 0, false },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, LEFT, 'B' }, "aBbcd", 2, false },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, LEFT, 'B', 'C' }, "aBCbcd", 3, false },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, BCK, BCK, 'B', 'C' }, "aBCd", 3, false },
                { new char[] { 'a', 'b', 'c', RIGHT, RIGHT  }, "abc", 3, true },
                { new char[] { 'a', 'b', 'c', RIGHT, RIGHT, 'd'  }, "abcd", 4, true },
                { new char[] { DEL }, "" , 0, true },
                { new char[] { DEL, DEL }, "" , 0, true },
                { new char[] { DEL, BCK }, "" , 0, true },
                { new char[] { 'a', 'b', 'c', DEL, DEL  }, "abc", 3, true },
                { new char[] { 'a', 'b', 'c', LEFT, LEFT, LEFT, DEL, DEL  }, "c", 0, false },
                { new char[] { 'a', 'b', 'c', LEFT, LEFT, LEFT, DEL, DEL, 'd'  }, "dc", 1, false },
                { new char[] { 'a', 'b', 'c', LEFT, DEL, DEL  }, "ab", 2, true },
                { new char[] { 'a', 'b', 'c', LEFT, LEFT, DEL  }, "ac", 1, false },
                { new char[] { HOME }, "" , 0 , true },
                { new char[] { END }, "" , 0 , true },
                { new char[] { HOME, 'a' }, "a" , 1 , true },
                { new char[] { END, 'a' }, "a" , 1 , true },
                { new char[] { HOME, END, HOME, END }, "" , 0 , true },
                { new char[] { 'a', 'b', 'c', HOME  }, "abc", 0, false },
                { new char[] { 'a', 'b', 'c', HOME, END  }, "abc", 3, true },
                { new char[] { RIGHT }, "" , 0 , true },
                { new char[] { LEFT }, "" , 0 , true },
                { new char[] { RIGHT, 'a' }, "a" , 1 , true },
                { new char[] { LEFT, 'a' }, "a" , 1 , true },

        };
    }

    @Test(dataProvider = "testInputsProvider")
    public void testInputs(char[] keys, String expectedCommand, Integer expectedCursorPosition, Boolean isCursorInEndLinePosition) {
        CommandRenderer commandRenderer = new CommandRenderer();
        for (int i=0; i<keys.length; i++) {
            switch (keys[i]) {
                case LEFT:
                    commandRenderer.onKeyLeft();
                    break;
                case RIGHT:
                    commandRenderer.onKeyRight();
                    break;
                case BCK:
                    commandRenderer.onBackSpace();
                    break;
                case DEL:
                    commandRenderer.onDeleteKey();
                    break;
                case HOME:
                    commandRenderer.onKeyHome();
                    break;
                case END:
                    commandRenderer.onKeyEnd();
                    break;
                default:
                    commandRenderer.onCharInsert((byte)keys[i]);
                    break;
            }
        }
        String command = commandRenderer.getCommand();
        Assert.assertNotNull(command);
        Assert.assertEquals(command, expectedCommand);
        Assert.assertEquals(Integer.valueOf(commandRenderer.getCursorPosition()), expectedCursorPosition);
        Assert.assertEquals(Boolean.valueOf(commandRenderer.isCursorInEndLinePosition()), isCursorInEndLinePosition);
        Assert.assertEquals(command.length(), commandRenderer.getCommandLength());
    }

}
