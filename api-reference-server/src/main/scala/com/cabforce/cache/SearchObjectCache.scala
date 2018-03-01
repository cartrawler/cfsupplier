package com.cabforce.cache

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.caching.LfuCache
import akka.http.caching.scaladsl.{Cache, CachingSettings, LfuCacheSettings}
import com.cabforce.core.BookingStatusEntry

import scala.concurrent.Await
import scala.concurrent.duration._

object SearchObjectCache {
  implicit val system = ActorSystem()

  val logger = Logging(system, getClass)

  val defaultCachingSettings = CachingSettings(system)

  val lfuCacheSettings : LfuCacheSettings = defaultCachingSettings.lfuCacheSettings
    .withInitialCapacity(25)
    .withMaxCapacity(50)
    .withTimeToLive(2.days)
    .withTimeToIdle(1.9.days)

  val cachingSettings : CachingSettings = defaultCachingSettings.withLfuCacheSettings(lfuCacheSettings)

  val lfuSearchCache: Cache[String, Object] = LfuCache(cachingSettings)

  val lfuBookingCache: Cache[String, Object] = LfuCache(cachingSettings)

  def pcache(searchId: String, obj: com.cabforce.core.Product): com.cabforce.core.Product = {
    Await.result(lfuSearchCache.get(searchId, () => obj), 3.seconds).asInstanceOf[com.cabforce.core.Product]
  }

  def scache(searchId: String, createId: String, obj: BookingStatusEntry): BookingStatusEntry = {
    lfuSearchCache.getOptional(searchId).orElseThrow(() => throw new RuntimeException(s"Offer $searchId / $createId does not exists")).thenApply(a => {
      Await.result(lfuBookingCache.get(createId, () => obj), 3.seconds).asInstanceOf[BookingStatusEntry]
    }).toCompletableFuture.get.asInstanceOf[BookingStatusEntry]
  }

  def scache(createId: String) : BookingStatusEntry = {
    lfuBookingCache.getOptional(createId).orElseThrow(() => throw new RuntimeException(s"Booking $createId does not created")).toCompletableFuture.get.asInstanceOf[BookingStatusEntry]
  }

  def supdate(createId: String, obj: BookingStatusEntry) : BookingStatusEntry = {
    lfuBookingCache.remove(createId)
    Await.result(lfuBookingCache.get(createId, () => obj), 3.seconds).asInstanceOf[BookingStatusEntry]
  }
}
