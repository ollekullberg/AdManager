
package com.programmera.admanager.http {

  import javax.servlet.http.HttpServlet
  import javax.servlet.http.HttpServletRequest
  import javax.servlet.http.HttpServletResponse

  import com.programmera.admanager.service.AdService
  import com.programmera.admanager.service.AdLocator
 
  class FrontController extends HttpServlet
  {
    
    override def init() {
      val filePath = getServletContext().getRealPath("/WEB-INF/classes/ids_and_uris.txt")
      AdLocator.initAdMap(filePath)
    }

    override def doGet(req : HttpServletRequest, resp : HttpServletResponse) = {
      // Convert URI to wrapper object
      val adUri = AdUri(req.getRequestURL.toString)

      // Get the HTML to put in response
      val adHtml = AdService.getAdHtmlFromId(adUri, getCookieId(req))
      
      // Send response
      resp.getWriter().print(adHtml)
    }

 
    // Get cookie ID 
    def getCookieId(req : HttpServletRequest): String = { 
      val CookiePattern = """SESSIONID=(\w+)$""".r
      val c = CookiePattern findFirstIn req.getHeader("cookie")
      c.getOrElse("SESSIONID=Nothing").substring(10)
    }
  }
  
  /**
   * Helper class in the HTTP domain
   */
  case class AdUri (uri: String) {
    // A HttpUri must start with http://
    require( uri.indexOf("http://") == 0)

    // Get the last chars in the URI 
    // (to check what operation we have)
    def getOperation: String = {
      val OpPattern= """\.(\w+)$""".r
      val op = OpPattern findFirstIn uri
      op.getOrElse(".Noting").substring(1)
    }

    // Get base URI
    def getBaseUri: String = {
      val pos = uri.lastIndexOf('/')
      uri.substring(0,pos-2) // -2 to erase /a/
    }

    // Get the ID for the ad
    def getAdId: Int = {
      val TailPattern= """/(\d+)\.(\w+)$""".r
      val NumPattern= """(\d+)""".r
      val tail = TailPattern findFirstIn uri
      val id = NumPattern findFirstIn tail.getOrElse("0")
      id.getOrElse("0").toInt
    }
  }
     
}




