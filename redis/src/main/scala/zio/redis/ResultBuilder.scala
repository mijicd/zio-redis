package zio.redis

import zio.ZIO
import zio.redis.ResultBuilder.NeedsReturnType
import zio.schema.Schema

sealed trait ResultBuilder {

  /**
   * The purpose of method to give a hint what is the error if you don't call `returning` method
   */
  final def map(f: Nothing => Any)(implicit nrt: NeedsReturnType): ZIO[Any, Nothing, Nothing] = ???

  /**
   * The purpose of method to give a hint what is the error if you don't call `returning` method
   */
  final def flatMap(f: Nothing => Any)(implicit nrt: NeedsReturnType): ZIO[Any, Nothing, Nothing] = ???
}

object ResultBuilder {

  @annotation.implicitNotFound("Use `returning[A]` to specify method's return type")
  final abstract class NeedsReturnType

  trait ResultSchemaBuilder extends ResultBuilder {
    def returning[R: Schema]: ZIO[RedisExecutor, RedisError, R]
  }

  trait ResultSchemaBuilder1[F[_]] extends ResultBuilder {
    def returning[R: Schema]: ZIO[RedisExecutor, RedisError, F[R]]
  }

  trait ResultSchemaBuilder2[F[_, _]] extends ResultBuilder {
    def returning[R1: Schema, R2: Schema]: ZIO[RedisExecutor, RedisError, F[R1, R2]]
  }

  trait ResultSchemaBuilder3[F[_, _, _]] extends ResultBuilder {
    def returning[R1: Schema, R2: Schema, R3: Schema]: ZIO[RedisExecutor, RedisError, F[R1, R2, R3]]
  }

  trait ResultOutputBuilder extends ResultBuilder {
    def returning[R: Output]: ZIO[RedisExecutor, RedisError, R]
  }
}
