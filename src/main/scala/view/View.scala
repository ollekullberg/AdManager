
package com.programmera.admanager.view {

  import com.programmera.admanager.domain.Ad
  import com.programmera.admanager.http.AdUri

  object AdView {

    // Build HTML from the Ad
    def buildIframe(adUri: AdUri, ad: Ad): String = {
      val clickUri = adUri.getBaseUri + "/a/" + ad.id + ".click" 
      val debug = "Ad ID=" + adUri.getAdId + ", op=" + adUri.getOperation + ", " + "base=  "+ adUri.getBaseUri + ", click URI=" + clickUri

"""<HTML>
<HEAD><TITLE>AdManager</TITLE>
  <script type="text/javascript">
     function call_url() {
       // New call on this frame to gather statistics
       clickImg=  new Image;
       clickImg.src='%s' 

       // Redirect parent to get to the new site
       window.open('%s','mywindow','width=600,height=600')
     }
  </script>
</HEAD>
<BODY>
<br/><a href="javascript:call_url();" ><img border=0 src="%s" /></a>
</BODY>
</HTML>""".format(clickUri, ad.redirectUri, ad.imgUri)
    }
  

  }

}




