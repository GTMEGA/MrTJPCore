/*
 * Copyright (c) 2014.
 * Created by MrTJP.
 * All rights reserved.
 */
package mrtjp.core.vec

import codechicken.lib.raytracer.IndexedCuboid6
import codechicken.lib.render.CCModel
import codechicken.lib.vec._
import net.minecraft.util.ResourceLocation
import scala.collection.JavaConversions._

object VecLib {
  def buildCubeArray(
      xSize: Int,
      zSize: Int,
      box: Cuboid6,
      expand: Vector3
  ): Array[IndexedCuboid6] = {
    import box.{max, min}
    min.multiply(1 / 16d)
    max.multiply(1 / 16d)
    expand.multiply(1 / 16d)
    val data = new Array[IndexedCuboid6](xSize * zSize)
    for (i <- 0 until data.length) {
      val x = i % xSize
      val z = i / zSize
      val dx = (max.x - min.x) / xSize
      val dz = (max.z - min.z) / zSize
      val min1 = new Vector3(min.x + dx * x, min.y, min.z + dz * z)
      val max1 = new Vector3(min1.x + dx, max.y, min1.z + dz)
      data(i) = new IndexedCuboid6(i, new Cuboid6(min1, max1).expand(expand))
    }
    data
  }

  def orientT(orient: Int) = {
    var t = Rotation.sideOrientation(orient % 24 >> 2, orient & 3)
    if (orient >= 24) t = new Scale(-1, 1, 1).`with`(t)
    t.at(Vector3.center)
  }

  def parseCorrectedModel(loc: String) = {
    val models = mapAsScalaMap(
      CCModel.parseObjModels(new ResourceLocation(loc))
    )
    models.map(m => m._1 -> m._2.backfacedCopy())
  }

  def finishModel(m: CCModel) = {
    m.computeNormals()
    m.shrinkUVs(0.0005)
  }

  def loadModel(loc: String) = finishModel(
    CCModel.combine(parseCorrectedModel(loc).values)
  )

  def loadModels(loc: String) = {
    val models = parseCorrectedModel(loc)
    models.values.foreach(finishModel)
    models
  }
}
