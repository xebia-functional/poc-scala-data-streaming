package com.fortyseven.typesafeconfiguration

import cats.effect.kernel.Async
import com.fortyseven.coreheaders.ConfigurationLoaderHeader
import com.fortyseven.coreheaders.configuration.JobProcessorConfiguration
import pureconfig.{ConfigReader, ConfigSource}
import pureconfig.module.catseffect.syntax.*

import scala.reflect.ClassTag

abstract class TypesafeConfigurationLoader[F[_]: Async, A: ConfigReader: ClassTag](path: String)
    extends ConfigurationLoaderHeader[F, A]:

  override def load(configurationPath: Option[String]): F[A] =
    configurationPath.fold(ConfigSource.default)(ConfigSource.resources).at(path).loadF[F, A]()
