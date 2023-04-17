package com.fortyseven.core

import com.fortyseven.coreheaders.DoSomething

case class Something (s: String) extends DoSomething:
  override def printToConsole(): Unit = println(s)