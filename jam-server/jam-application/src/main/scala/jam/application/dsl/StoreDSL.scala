package jam.application.dsl

import jam.application.dsl.Result.Result

trait StoreDSL[F[_], Key, E, A] {
  def getAll: Result[F, E, Seq[A]]
  def put(key: Key, obj: A): Result[F, E, A]
  def get(id: Key): Result[F, E, A]
  def delete(id: Key): Result[F, E, Unit]
}