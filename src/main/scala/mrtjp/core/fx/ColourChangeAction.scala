/*
 * Copyright (c) 2015.
 * Created by MrTJP.
 * All rights reserved.
 */
package mrtjp.core.fx

import codechicken.lib.vec.Vector3
import mrtjp.core.fx.particles.CoreParticle

trait TColourParticle extends CoreParticle {
  var rgb = Vector3.one.copy

  def red = rgb.x
  def green = rgb.y
  def blue = rgb.z

  def red_=(r: Double) { rgb.x = r.toFloat }
  def green_=(g: Double) { rgb.y = g.toFloat }
  def blue_=(b: Double) { rgb.z = b.toFloat }

  def setRGB(r: Double, g: Double, b: Double) {
    red = r
    green = g
    blue = b
  }
}

class ColourChangeToAction extends ParticleAction {
  var target = Vector3.zero
  var duration = 0.0

  override def canOperate(p: CoreParticle) = p.isInstanceOf[TColourParticle]

  override def operate(p: CoreParticle, time: Double) {
    val c = p.asInstanceOf[TColourParticle]

    if (time < duration) {
      val drgb = target.copy.subtract(c.rgb)
      val speed =
        drgb.copy.multiply(1 / (duration - time)).multiply(deltaTime(time))
      c.rgb.add(speed)
    } else isFinished = true
  }

  override def compile(p: CoreParticle) {}

  override def copy =
    ParticleAction.changeColourTo(target.x, target.y, target.z, duration)
}

class ColourChangeForAction extends ParticleAction {
  var delta = Vector3.zero
  var duration = 0.0

  override def canOperate(p: CoreParticle) = p.isInstanceOf[TColourParticle]

  override def operate(p: CoreParticle, time: Double) {
    val c = p.asInstanceOf[TColourParticle]
    if (time < duration) c.rgb.add(delta.copy.multiply(deltaTime(time)))
    else isFinished = true
  }

  override def compile(p: CoreParticle) {}

  override def copy =
    ParticleAction.changeColourFor(delta.x, delta.y, delta.z, duration)
}
