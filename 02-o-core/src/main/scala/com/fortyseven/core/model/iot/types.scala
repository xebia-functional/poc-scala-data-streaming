package com.fortyseven.core.model.iot

object types:

  opaque type Coordinate <: Double = Double // Should be limited to earth coordinates
  opaque type Percentage <: Double = Double //Should be limited to 0.00 and 100.00
  opaque type Speed <: Double = Double // Should be typed better. Meters/second or Km/h?

  object Coordinate:
    def apply(coordinate: Double): Coordinate = coordinate
    extension (coordinate: Coordinate) def value: Double = coordinate

  object Percentage:
    def apply(percentage: Double): Percentage = percentage
    extension (percentage: Percentage) def value: Double = percentage

  object Speed:
    def apply(speed: Double): Speed = speed

    extension (speed: Speed) def value: Double = speed  

