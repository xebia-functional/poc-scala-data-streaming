package com.fortyseven.main

import com.fortyseven.core.Something

object Main:
  def main(args: Array[String]): Unit = Program.run(
    Something("This is a test!")
  )

