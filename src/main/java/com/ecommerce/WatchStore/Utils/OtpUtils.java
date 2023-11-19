package com.ecommerce.WatchStore.Utils;

import java.util.SplittableRandom;

public class OtpUtils {
    public String generateOtp(){
        SplittableRandom random = new SplittableRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0 ; i < 6; i++)
        {
            stringBuilder.append(random.nextInt(0,9));
        }
        return stringBuilder.toString();
    }

}
