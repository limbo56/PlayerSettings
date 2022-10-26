import me.limbo56.playersettings.util.Version;
import org.junit.Assert;
import org.junit.Test;

public class VersionTest {
  @Test
  public void testVersionsComparator() {
    Version v1_10_2 = Version.from("1.10.2");
    Assert.assertTrue(
        "Version 1.10.2 should be older than version 1.10.3", v1_10_2.isOlderThan("1.10.3"));
    Assert.assertTrue(
        "Version 1.8.8 should be older than version 1.14",
        Version.from("1.8.8").isOlderThan("1.14"));
    Assert.assertTrue(
        "Version 1.13 should be older than version 1.14", Version.from("1.13").isOlderThan("1.14"));
    Assert.assertTrue(
        "Version 1.14 should be older than version 1.16", Version.from("1.14").isOlderThan("1.16"));
    Assert.assertEquals(
        "Version 1.10.2 should be equal to version 1.10.2", 0, v1_10_2.compareTo("1.10.2"));
    Assert.assertEquals("Version 1.10.2 should be newer than 1.9.2", 1, v1_10_2.compareTo("1.9.2"));
  }
}
