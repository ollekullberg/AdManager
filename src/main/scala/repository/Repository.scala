
/**
 * Holds the services of the application
 */
package com.programmera.admanager.repository {

  import com.programmera.admanager.domain.Ad
  import com.programmera.admanager.http.AdUri
  import com.programmera.admanager.view.AdView

  class Event(timestamp: java.util.Date)

  // Valid URIs
  case class AdEvent(adId: Int, surfer: String, timestamp: java.util.Date) extends Event(timestamp)

  // Bad URIs
  case class BadEvent(uri: String, timestamp: java.util.Date) extends Event(timestamp)

  /**
   * A singleton that manages the repository
   */
  object Repository {
    // A list of banner views
    private var imprList = List[AdEvent]()
    // A list of banner clicks
    private var clickList = List[AdEvent]()
    // A list of bad URIs
    private var badList = List[BadEvent]()

    // ========================
    // Methods to store the events
    // ========================
    def storeImpression(ad: Ad, surfer: String) { 
      imprList = AdEvent(ad.id, surfer, new java.util.Date()) :: imprList
    }
    def storeClick(ad: Ad, surfer: String) {
      clickList = AdEvent(ad.id, surfer, new java.util.Date()) :: clickList
    }
    def storeBadUri(adUri: AdUri) { 
      badList =   BadEvent(adUri.uri, new java.util.Date()) :: badList
    }

    // ========================
    // Methods to extract statistics
    // ========================
    def calcTotalImpressions = imprList.size 
    def calcTotalClicks = clickList.size
    def calcFailedUriCalls = badList.size

    /**
     * Will calculate unique clicks or unique impressions
     */
    def calcUnique(l: List[AdEvent]) = {
      // Case classes provide implementation of equals() 
      case class SurferAdCombo(adId: Int, surfer: String)
  
      // A set will only hold unique elements
      val uniqueSet = collection.mutable.Set[SurferAdCombo]()

      // Add all elements as SurferAdCompos
      l.foreach { event =>
        uniqueSet += SurferAdCombo(event.adId, event.surfer) 
      } 
 
      // Return size
      uniqueSet.size
    }

    // ========================
    // Presentation
    // ========================
    def toHtml = {
      val statsHtml ="""<H2>Statistics</H2>
        Total Impressions: %s<BR/>
        Unique Impressions: %s<BR/>
        Total Clicks: %s<BR/>
        Unique Clicks: %s<BR/>
        Unreadable URIs: %s<HR/>""".format(
        calcTotalImpressions,
        calcUnique(imprList), 
        calcTotalClicks, 
        calcUnique(clickList), 
        calcFailedUriCalls)

      val imprHtml = imprList.mkString("<H2>Impressions</H2>","<BR/>","<HR/>")
      val clickHtml = clickList.mkString("<H2>Clicks</H2>","<BR/>","<HR/>")
      val badHtml = badList.mkString("<H2>Bad requests</H2>","<BR/>","<HR/>")

      "<HTML><HEAD><TITLE>AdManager Logs</TITLE></HEAD><BODY><H1>AdManager Logs</H1><HR>" + statsHtml + imprHtml + clickHtml + badHtml + "</BODY></HTML>"
    } 
  }
    
}




