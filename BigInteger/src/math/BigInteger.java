package math;

public class BigInteger {

	boolean negative;
	int numDigits;
	DigitNode front;
	
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	public void addInt(int a){
		front = new DigitNode(a,front);
		numDigits++;		
	}
	
	public void addLast(int a){
		if(this.front==null){
			this.front=new DigitNode(a,null);
			return;
		}
		for(DigitNode temp = this.front;temp!=null;temp=temp.next){
			if(temp.next==null){
				temp.next=new DigitNode(a,null);
				return;
			}
		}
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		integer=integer.trim();
		BigInteger a = new BigInteger();
		int zerodisplace=0;
		if(integer.length()==0){
			throw new IllegalArgumentException("Pleae enter a number!");
		}
		if(integer.charAt(0)=='-'){
			a.negative = true;
		}
		for(int i=0;i>-1;i++){
			if(integer.charAt(i)<48 && integer.charAt(i)>57){
				throw new IllegalArgumentException();
			}
			
			if(integer.charAt(i)!='0'&&integer.charAt(i)!='+'&&integer.charAt(i)!='-'){
				break;
			}
			if(i==integer.length()-1){
				break;
			}
			zerodisplace=i+1;
		}
		for(int i=zerodisplace;i<integer.length();i++){
			if(integer.charAt(i)==' '||integer.charAt(i)=='-'||integer.charAt(i)=='+'){
				System.out.println("caught a +,-,or ' ' at " + i +" with word "+integer+" with char "+integer.charAt(i));
			} else if(Character.isDigit(integer.charAt(i))){
				a.addInt(integer.charAt(i)-'0');
			} else{
				throw new IllegalArgumentException();
			}
		}
		// THE FOLLOWING LINE IS A PLACEHOLDER SO THE PROGRAM COMPILES
		// YOU WILL NEED TO CHANGE IT TO RETURN THE APPROPRIATE BigInteger
		return a;
	}
	
	/**
	 * Adds an integer to this integer, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY this integer.
	 * NOTE that either or both of the integers involved could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param other Other integer to be added to this integer
	 * @return Result integer
	 */
	public static BigInteger addSame(BigInteger tHis, BigInteger other,BigInteger temp){ //adding unlike signs
		boolean carry=false;
		DigitNode a,b;
		//Initializes the carry 
		//sets digitnode a as the larger number
		if(tHis.numDigits>other.numDigits){
			a=tHis.front;
			b=other.front;
		} else {
			b=tHis.front;
			a=other.front;
		}
		for(;a!=null;a=a.next){
			int sumTemp;
			if(b==null){
				sumTemp = 0;
			} else {
				sumTemp = b.digit;
			}
			//sums the current digit
			int sum=a.digit + sumTemp;
			//adds the carry, if true
			if(carry){
				sum++; //adds the carry 
			} 
			carry=sum>9;//sets the new carry for the next digit
			if(sum>9){
				sum=sum-10;//parses for the digit
			}
			temp.addLast(sum);
			if(a.next==null&&carry){
				temp.addLast(1);
			}
			if(b!=null){
				b=b.next;
			}
		}
		return temp;
	}	
	public static boolean isBigger(BigInteger a, BigInteger b)
			throws IllegalArgumentException{
		if(a.numDigits>b.numDigits){
			return true;
		} else if(a.numDigits<b.numDigits) {
			return false;
		} else {
			for(int i=a.numDigits;i>0;i--){
				int j=0;
				DigitNode d = a.front;
				DigitNode e = b.front;
				for(DigitNode c = a.front;c!=null;c=c.next){
					j++;
					if(j==i){
						if(d.digit>e.digit){
							return true;
						} else if (d.digit<e.digit){
							return false;
						}
						break;
					}
					d=d.next;
					e=e.next;
							
				}
			}
			throw new IllegalArgumentException(); 
		}
	}
	public static BigInteger subtract(BigInteger tHis, BigInteger other,BigInteger temp){ //adding unlike signs
		boolean carry=false;
		DigitNode a,b;
		//Initializes the carry 
		//sets digitnode a as the larger number
		Boolean bigger=false;
		try{
			bigger = isBigger(tHis,other);
		} catch (IllegalArgumentException e){
			BigInteger zero = new BigInteger();
			zero.front = new DigitNode(0,null);
			return zero;
		}
		if(bigger){
			a=tHis.front;
			b=other.front;
			if(tHis.negative){
				temp.negative=true;
			}
		} else {
			b=tHis.front;
			a=other.front;
			if(other.negative){
				temp.negative=true;
			}
		}
		for(;a!=null;){
			int pos = a.digit;
			int neg;
			if(b==null){
				neg = 0;
			} else {
				neg = b.digit;
			}
			if(carry){
				pos--;
			}
			int difference= pos - neg;
			carry=difference<0;//sets the new carry for the next digit
			if(difference<0){
				difference=10+difference;//parses for the digit
			}
			if(!(b==null&&a.next==null&&difference==0)){ //parses the leading zeroes 
				temp.addLast(difference);
			}
			if(b!=null){
				b=b.next;
			}
			if(a!=null){
				a=a.next;
			}
		}
		return temp;
	}
	public BigInteger add(BigInteger other) {
		//creates temp linked list to return
		BigInteger temp = new BigInteger();
		//for positive + positive and negative + negative
		
		if((!this.negative&&!other.negative)||(this.negative&&other.negative)){ 
			temp = addSame(this,other,temp);
			if(this.negative&&other.negative){
				temp.negative=true;
			}
		} else { 
			temp = subtract(this,other,temp);
		}
		return temp ;
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the given BigInteger
	 * with this BigInteger - DOES NOT MODIFY this BigInteger
	 * 
	 * @param other BigInteger to be multiplied
	 * @return A new BigInteger which is the product of this BigInteger and other.
	 */
	public BigInteger multiply(BigInteger other) {
		BigInteger minus = new BigInteger();
		minus.front = new DigitNode(1,null);
		minus.negative = true;
		BigInteger temp = this;
		BigInteger factor = this;
		other.front.digit--;
		while(!(other.front.digit==0 && other.front.next == null)){
			temp = temp.add(factor);
			other = other.add(minus);
			System.out.println(" i got here! the running sum is " + temp + " where the addition factor is "+ factor + " and the subtracting factor is " + other + "and the  minus factor is "+minus);
			
		}
		// THE FOLLOWING LINE IS A PLACEHOLDER SO THE PROGRAM COMPILES
		// YOU WILL NEED TO CHANGE IT TO RETURN THE APPROPRIATE BigInteger
		return temp;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		
		return retval;
	}
	
}