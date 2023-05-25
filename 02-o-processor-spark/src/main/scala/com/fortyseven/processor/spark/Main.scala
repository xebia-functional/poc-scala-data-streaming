package com.fortyseven.processor.spark

import cats.effect.{IO, IOApp}


object Main extends IOApp.Simple:
  override def run: IO[Unit] =
    for
      _          <- IO.println("Starting Spark")
      sparkFiber <- new DataProcessor[IO].run()
    yield ()
