package com.fortyseven.coreheaders

trait ConfigHeader[F[_], A]:

  def load: F[A]
