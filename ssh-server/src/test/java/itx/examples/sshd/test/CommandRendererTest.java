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
                { new char[] {}, "" },
                { new char[] { 'a', 'b', 'c'  }, "abc" },
                { new char[] { 'a', 'b', 'c', BCK  }, "ab" },
                { new char[] { 'a', 'b', 'c', BCK, BCK  }, "a" },
                { new char[] { 'a', 'b', 'c', BCK, BCK, BCK  }, "" },
                { new char[] { 'a', 'b', 'c', BCK, BCK, BCK, BCK  }, "" },
                { new char[] { BCK, BCK, BCK, BCK  }, "" },
                { new char[] { BCK, LEFT, RIGHT, BCK  }, "" },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, RIGHT, RIGHT, LEFT }, "abcd" },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, BCK }, "acd" },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, BCK, BCK }, "cd" },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, BCK, BCK, BCK }, "cd" },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, BCK, BCK, BCK, BCK }, "cd" },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, LEFT, 'B' }, "aBbcd" },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, LEFT, LEFT, 'B', 'C' }, "aBCbcd" },
                { new char[] { 'a', 'b', 'c', 'd', LEFT, BCK, BCK, 'B', 'C' }, "aBCd" },
                { new char[] { 'a', 'b', 'c', RIGHT, RIGHT  }, "abc" },
                { new char[] { 'a', 'b', 'c', RIGHT, RIGHT, 'd'  }, "abcd" },
        };
    }

    @Test(dataProvider = "testInputsProvider")
    public void testInputs(char[] keys, String expectedCommand) {
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
    }

}
