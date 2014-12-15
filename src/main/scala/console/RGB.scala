package me.mtrupkin.console

import play.api.libs.json._

/**
 * Created by mtrupkin on 12/14/2014.
 */
case class RGB(r: Int, g: Int, b:Int)

object RGB {
  implicit object RGBColorFormat extends Format[RGB] {
    def reads(json: JsValue): JsResult[RGB] = JsSuccess(json.as[String])
    def writes(u: RGB): JsValue = JsString(u.toString)
  }

  implicit def toString(rgb: RGB): String = {
    import rgb._
    f"#${r}%02X$g%02X$b%02X"
  }

  implicit def toRGB(s: String): RGB = {
    def next(s0: String): String = {
      s0.substring(0, 2)
    }

    def toInt(h: String): Int = {
      Integer.parseInt(h, 16)
    }

    val r = next(s.substring(1))
    val g = next(s.substring(3))
    val b = next(s.substring(5))

    RGB(toInt(r), toInt(g), toInt(b))
  }
}

object Colors {
  val Black = RGB(0, 0, 0)
  val White = RGB(255, 255, 255)
  val LightGrey = RGB(126, 126, 126)
  val Yellow = RGB(255, 255, 0)
  val Blue = RGB(0, 0, 255)
  val Red = RGB(255, 0, 0)
  val Green = RGB(0, 255, 0)
  val LightYellow = RGB(126, 126, 0)
  val LightBlue = RGB(21, 105, 199)
  val LightRed = RGB(126, 0, 0)
  val LightGreen = RGB(0, 126, 0)
}
