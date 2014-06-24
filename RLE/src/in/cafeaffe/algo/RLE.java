/**
 * 
 */
package in.cafeaffe.algo;

/**
 * @author gbm
 *
 */
public class RLE {
	
	public static void main(String[] args){
		String s = "AABBBDCCCXCCCYYYYYYYYYYYYYYYYYYYYYYYYYYZ";
		s = s + "\0";
		char[] charArr = s.toCharArray();
		doRle(charArr, true);
		System.out.println(new String(charArr));
		
		s = "ABC";
		s = s + "\0\0\0";
		charArr = s.toCharArray();
		doRle(charArr, true);
		System.out.println(new String(charArr));
		
		s = "AABBBBCCC";
		s = s + "\0";
		charArr = s.toCharArray();
		doRle(charArr, true);
		System.out.println(new String(charArr));
		

		s = "ABCDDDDDDEFFG";
		s = s + "\0";
		charArr = s.toCharArray();
		doRle(charArr, true);
		System.out.println(new String(charArr));
	} 
	
	public static void doRle(char[] charArr, boolean repeated){
		if(repeated){
			int idx = 0;
			while(true){
				int c = 1;
				if(charArr[idx] == '\0'){
					doRle(charArr, false);
					return;
				}
				while(charArr[idx] == charArr[++idx]){
					c++;
				}
				if(c == 1){
					if(charArr[idx] == '\0'){
						doRle(charArr, false);
						return;
					}
					continue;
				} else {
					char[] temp = String.valueOf(c).toCharArray();
					for(int i = 0;  i < temp.length; i++)
						charArr[idx+1-c+i] = temp[i];
					
					int startIndex = idx+1-c+temp.length;
					int copyFrom = idx;
					if(startIndex == copyFrom){
						continue;
					}
					for(int i = copyFrom; charArr[i] != '\0' ; i++){
						charArr[startIndex] = charArr[i];
						startIndex++;
					}
					charArr[startIndex] = '\0';
					idx = idx+1-c+temp.length;
				}
			}
		} else {
			int idx = 0;
			int len = 0;
			while(charArr[idx++] != '\0'){
				len++;
			}
			idx = 0;
			while(charArr[idx] != '\0'){
				if(Character.isDigit(charArr[idx])) {
					idx++;
					continue;
				}
				if(Character.isDigit(charArr[idx+1])){
					//Already counted -- repeated chars
					idx++;
					continue;
				}
				for(int i = len; i > idx+1; i--){
					charArr[i] = charArr[i-1];
				}
				charArr[idx+1] = '1';
				len += 1;
				idx +=2;
				if(idx >= len){
					if(len >= charArr.length){
						//The array is packed properly!
						return;
					}
					charArr[len] = '\0';
					return;
				}
			}
		}
	}
}
