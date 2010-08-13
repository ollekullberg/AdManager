
/**
 * Holds the services of the application
 */
package com.programmera.admanager.repository {

  import com.programmera.admanager.domain.Ad
  import com.programmera.admanager.http.AdUri
  import com.programmera.admanager.view.AdView

  // Represents a banner view
  case class ImpressionEvent(adId: Int,  surfer: String, timestamp: java.util.Date)

  // Represents a banner click
  case class ClickEvent(adId: Int, surfer: String, timestamp: java.util.Date)

  case class BadEvent(uri: String, timestamp: java.util.Date)
  /**
   * A singleton that manages the repository
   */
  object Repository {
    private var imprMap = Map[Int,ImpressionEvent]()
    private var clickMap = Map[Int,ClickEvent]()
    private var badList = List[BadEvent]()

    def storeImpression(ad: Ad, surfer: String) { 
      imprMap += ad.id -> ImpressionEvent(ad.id, surfer, new java.util.Date())
    }
    def storeClick(ad: Ad, surfer: String) {
      clickMap += ad.id -> ClickEvent(ad.id, surfer, new java.util.Date())
    }
    def storeBadUri(adUri: AdUri) { 
      badList =   BadEvent(adUri.uri, new java.util.Date()) :: badList
    }


    def toHtml = {
      val imprHtml = imprMap.map(e => e._2.toString ).mkString("<H2>Impressions</H2>","<BR/>","<HR/>")
      val clickHtml = clickMap.map(e => e._2.toString ).mkString("<H2>Clicks</H2>","<BR/>","<HR/>")
      val badHtml = badList.map(e => e.toString ).mkString("<H2>Bad requests</H2>","<BR/>","<HR/>")

      "<HTML><HEAD><TITLE>AdManager Logs</TITLE></HEAD><BODY><H1>AdManager Logs</H1><HR>" + imprHtml + clickHtml + badHtml + "</BODY></HTML>"
    } 
  }
    
}




