package com.wekebere.helpers;
import java.math.BigInteger;
import java.util.Date;
import java.util.Random;
public class RandNumbers {
public BigInteger generateRandomDate(){
Date d = new Date();
BigInteger randomBigInteger = nextRandomBigInteger(BigInteger.valueOf(d.getTime()));
return randomBigInteger;
 }
private BigInteger nextRandomBigInteger(BigInteger n) {
Random rand = new Random();
BigInteger result = new BigInteger(n.bitLength(), rand);
while( result.compareTo(n) >= 0 ) {
result = new BigInteger(n.bitLength(), rand);
}
return result;
}     
}
