package testing;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestBoardState.class, TestGame.class, TestPiece.class, TestTimePanel.class,
		TestImagePanel.class })
public class AllTests {
	public static void main(String args[]) {
		org.junit.runner.JUnitCore.main("testing.AllTests");
	}
}
