package itx.examples.sshd.test;

import itx.examples.sshd.commands.repl.CommandRenderer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CommandRendererTest {

    private final static char LEFT  = 1;
    private final static char RIGHT = 2;
    private final static char BCK = 3;

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
        commandRenderer.onCharInsert('a');
        commandRenderer.onCharInsert('b');
        commandRenderer.onCharInsert('c');
        String command = commandRenderer.getCommand();
        Assert.assertNotNull(command);
        Assert.assertEquals(command, "abc");
        commandRenderer.reset();
        command = commandRenderer.getCommand();
        Assert.assertNotNull(command);
        Assert.assertEquals(command, "");
        commandRenderer.onCharInsert('D');
        commandRenderer.onCharInsert('C');
        commandRenderer.onCharInsert('E');
        command = commandRenderer.getCommand();
        Assert.assertNotNull(command);
        Assert.assertEquals(command, "DCE");
    }


    @DataProvider(name = "testInputsProvider")
    public static Object[][] getDataForTestInputs() {
        return new Object[][] {
                { new char[] {}, "" , 0 },
                { new char[] { 'a', 'b', 'c'  }, "abc", 3 },
                { new char[] { 'a', 'b', 'c', BCK  }, "ab", 2 },
                { new char[] { 'a', 'b', 'c', BCK, BCK  }, "a", 1 },
                { new char[] { 'a', 'b', 'c', BCK, BCK, BCK  }, "", 0 },
                { new char[] { 'a', 'b', 'c', BCK, BCK, BCK, BCK  }, "", 0 },
                { new char[] { BCK, BCK, BCK, BCK  }, "", 0 },
                { new char[] { BCK, LEFT, RIGHT, BCK  }, "", 0 },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, RIGHT, RIGHT, LEFT }, "abcd", 3 },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, BCK }, "acd", 1 },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, BCK, BCK }, "cd", 0 },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, BCK, BCK, BCK }, "cd", 0 },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, BCK, BCK, BCK, BCK }, "cd", 0 },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, LEFT, 'B' }, "aBbcd", 2 },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, LEFT, 'B', 'C' }, "aBCbcd", 3 },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, BCK, BCK, 'B', 'C' }, "aBCd", 3 },
                { new char[] { 'a', 'b', 'c', RIGHT, RIGHT  }, "abc", 3 },
                { new char[] { 'a', 'b', 'c', RIGHT, RIGHT, 'd'  }, "abcd", 4 },
        };
    }

    @Test(dataProvider = "testInputsProvider")
    public void testInputs(char[] keys, String expectedCommand, Integer expectedCursorPosition) {
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
                default:
                    commandRenderer.onCharInsert(keys[i]);
                    break;
            }
        }
        String command = commandRenderer.getCommand();
        Assert.assertNotNull(command);
        Assert.assertEquals(command, expectedCommand);
        Assert.assertEquals(Integer.valueOf(commandRenderer.getCursorPosition()), expectedCursorPosition);
    }

}
