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
   private CPosition oTarget;
   private Integer iSize = 5;

   public CWalker(CPosition pos, CPosition target) {
       this.oPos = pos;
       this.oTarget = target;
   }

   public CPosition getPosition()
   {
       return oPos;
   }

   public CPosition getTarget()
   {
       return oTarget;
   }

   public Integer getSize() {
       return iSize;
   }
}
