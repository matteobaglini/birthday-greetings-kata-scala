package it.kata.birthday_greetings

import java.text.SimpleDateFormat
import java.util.{Calendar, Date, GregorianCalendar}

case class XDate(date: Date) {

  def day: Int = getPartOfDate(Calendar.DAY_OF_MONTH)

  def month: Int = 1 + getPartOfDate(Calendar.MONTH)

  def isSameDay(anotherDate: XDate): Boolean =
    anotherDate.day == this.day && anotherDate.month == this.month

  private def getPartOfDate(part: Int) = {
    val calendar = new GregorianCalendar
    calendar.setTime(date)
    calendar.get(part)
  }
}

object XDate {
  def apply(): XDate = new XDate(new Date)
  def apply(raw: String): XDate =
    new XDate(new SimpleDateFormat("yyyy/MM/dd").parse(raw))
}
