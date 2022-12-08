import me.limbo56.playersettings.util.Version;
import org.junit.Assert;
import org.junit.Test;

public class VersionTest {
  @Test
  public void testVersionsComparator() {
    String minecraft_v1_10_2 = "1.10.2";
    String minecraft_v1_14 = "1.14";
    assertOlder(minecraft_v1_10_2, "1.10.3");
    assertEqual(minecraft_v1_10_2, minecraft_v1_10_2);
    assertNewer(minecraft_v1_10_2, "1.9.2");
    assertOlder("1.8.8", minecraft_v1_14);
    assertOlder("1.13", minecraft_v1_14);
    assertOlder(minecraft_v1_14, "1.16");

    String plugin_v6_2_0 = "6.2.0";
    assertNewer(plugin_v6_2_0, "6.1.2");
    assertEqual(plugin_v6_2_0, plugin_v6_2_0);
  }

  private void assertNewer(String version, String version2) {
    Assert.assertTrue(
        "Version " + version + " should be newer than version " + version2,
        Version.from(version).compareTo(version2) > 0);
  }

  private void assertOlder(String version, String version2) {
    Assert.assertTrue(
        "Version " + version + " should be older than version " + version2,
        Version.from(version).compareTo(version2) < 0);
  }

  private void assertEqual(String version, String version2) {
    Assert.assertEquals(
        "Version " + version + " should be equal to version " + version2,
        0,
        Version.from(version).compareTo(version2));
  }
}
