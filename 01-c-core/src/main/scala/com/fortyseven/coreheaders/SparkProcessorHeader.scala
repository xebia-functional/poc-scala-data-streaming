package com.fortyseven.coreheaders

import com.fortyseven.coreheaders.configuration.SparkProcessorConfiguration

trait SparkProcessorHeader[F[_]]:
  def process(config: ConfigurationLoaderHeader[F, SparkProcessorConfiguration]): F[Unit]
  
