package Source;

import Util.CPosition;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 27.09.13
 * Time: 14:09
 */
public class CWalker {
   private CPosition oPos;

   public CWalker(CPosition pos) {
       this.oPos = pos;
   }

   public CPosition getPosition()
   {
       return oPos;
   }
}
