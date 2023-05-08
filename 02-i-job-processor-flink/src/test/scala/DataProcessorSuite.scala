import munit.CatsEffectSuite
import com.dimafeng.testcontainers.lifecycle.and
import com.dimafeng.testcontainers.{KafkaContainer, SchemaRegistryContainer}
import com.dimafeng.testcontainers.munit.TestContainersForAll
import org.testcontainers.lifecycle.TestDescription
import org.testcontainers.containers.Network
class DataProcessorSuite extends CatsEffectSuite with TestContainersForAll:

  override type Containers = KafkaContainer `and` SchemaRegistryContainer

  override val suiteDescription = new TestDescription:
    override def getTestId = "a"
    override def getFilesystemFriendlyName = "a"

  override def startContainers(): Containers = {
    val kafkaVersion = "6.2.0"
    val hostName = "kafka-broker"

    val network: Network = Network.newNetwork()

    val kafkaContainer: KafkaContainer =
      KafkaContainer.Def(kafkaVersion).createContainer()

    kafkaContainer.container
      .withNetwork(network)
      .withNetworkAliases(hostName)

    kafkaContainer.start()

    val schemaRegistryContainer = SchemaRegistryContainer
      .Def(network, hostName, kafkaVersion)
      .start()

    kafkaContainer `and` schemaRegistryContainer
  }

  test("abc") {
    assert(true)
  }
