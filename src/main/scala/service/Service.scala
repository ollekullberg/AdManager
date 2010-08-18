
/**
 * Holds the services of the application
 */
package com.programmera.admanager.service {

  import com.programmera.admanager.domain.Ad
  import com.programmera.admanager.http.AdUri
  import com.programmera.admanager.view.AdView
  import com.programmera.admanager.repository.Repository

  /**
   * A singleton that serves as an entry point for the application.
   */
  object AdService {
    val clickOp ="click"
    val viewOp ="iframe"

    // Main method, returns HTML depending on operation
    def getAdHtmlFromId(adUri: AdUri, cookieId: String ): String =  {

      // Lookup the ID 
      val ad: Ad = AdLocator.findAdFromId(adUri.getAdId) 

      adUri.getOperation match {
        case "iframe" => {
          // Log request to repository
          Repository.storeImpression(ad, cookieId) 
          AdView.buildIframe(adUri, ad)
        }
        case "click" => {
          // Log request to repository
          Repository.storeClick(ad, cookieId) 
          AdView.buildClickHtml(ad)
        }
        case "log" => {
          Repository.toHtml
        }
        case _ => {
          // Log error to repository
          Repository.storeBadUri(adUri) 
          throw new IllegalArgumentException("Error, URL must end with \"click\" or \"iframe\".")
        } 
      }

      // Always return same content
      //"Ad ID=" + adUri.getAdId + ", op=" + adUri.getOperation + ", cookie=" + cookieId + "base=  "+ adUri.getBaseUri
    }
  }

  /**
   * A singleton that finds the Ad corresponding to the ID
   */
 
  object AdLocator {
    private var adMap = Map[Int, Ad]()

    def initAdMap(filePath: String ) =  {
      import scala.io.Source

      // Only initialize if first time
      if(adMap.size < 1){
        val file = new java.io.File(filePath)
        for(line <- Source.fromFile(file).getLines())
          addLineToMap(line)
      } 
    }
   
    private def addLineToMap(line: String) {
      val arr = line.split('#')
      val id: Int = arr(0).toInt
      val ad = Ad(id,arr(1),arr(2),arr(3))
      adMap += id -> ad
    }

    // Fetches the ad corresponding to the given ID
    def findAdFromId(id: Int) = adMap(id)

  }

}




