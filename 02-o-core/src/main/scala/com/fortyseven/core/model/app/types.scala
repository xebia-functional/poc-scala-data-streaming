package com.fortyseven.core.model.app

import java.util.UUID

object types:
  
  opaque type Kilometers <: Double = Double
  object Kilometers:
    def apply(kilometers: Double): Kilometers = kilometers
    extension (kilometers: Kilometers) def value: Double = kilometers
