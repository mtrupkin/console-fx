package me.mtrupkin.console

import me.mtrupkin.console.Colors._
import play.api.libs.json._

/**
 * Created by mtrupkin on 12/14/2014.
 */
case class ScreenChar(c: Char, fg: RGB = White, bg: RGB = Black)

object ScreenChar {
  implicit def ScToChar(sc: ScreenChar): Char = sc.c
  implicit def ScToString(sc: ScreenChar): String = sc.c.toString

  implicit object RGBColorFormat extends Format[Char] {
    def reads(json: JsValue): JsResult[Char] = json.validate[String].map(_.charAt(0))
    def writes(u: Char): JsValue = JsString(u.toString)
  }

  implicit val format = Json.format[ScreenChar]
}