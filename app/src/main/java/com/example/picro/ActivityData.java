package com.example.picro;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ActivityData {

    // ARRAY ID
    private static String[] record_id = {
        "Encounter",
        "What's Wrong with Secretary Kim",
        "Memories of the Alhambra",
        "Sky Castle",
        "Hotel Del Luna",
        "Abyss",
        "The Bride of Habaek",
        "Her Private Life",
        "Descendants of the Sun",
        "Strong Woman Do Bong Soon",
        "My Fellow Citizens",
        "The Bride of Habaek",
    };

    // ARRAY RECORD DATA
    private static String[] record_data = {
        "Encounter (Korean: 남자친구; RR: Namjachingu; lit. Boyfriend) is a 2018 South Korean television series starring Song Hye-kyo and Park Bo-gum. It aired on tvN's Wednesdays and Thursdays at 21:30 (KST) time slot from November 28, 2018 to January 24, 2019.",
        "What's Wrong with Secretary Kim? (Korean: 김비서가 왜 그럴까; RR: Kimbiseoga wae geureolkka) is a 2018 South Korean television series starring Park Seo-joon and Park Min-young",
        "Memories of the Alhambra (Korean: 알함브라 궁전의 추억; RR: Alhambeura gungjeonui chueok) is a 2018 South Korean television series, starring Hyun Bin",
        "Sky Castle (Korean: SKY 캐슬; RR: SKY Kaeseul; stylized as SKY Castle) is a 2018-2019 South Korean television series starring Yum Jung-ah, Lee Tae-ran, Yoon Se-ah, Oh Na-ra and Kim Seo-hyung. It aired on JTBC's Fridays and Saturdays at 23:00 KST time slot from November 23, 2018 to February 1, 2019.",
        "Hotel del Luna (Korean: 호텔 델루나; RR: Hotel delluna) is a 2019 South Korean television series, starring Lee Ji-eun and Yeo Jin-goo as the owner and manager, respectively, of the eponymous hotel that caters only to ghosts",
        "Abyss (Korean: 어비스; RR: Eobiseu) is a 2019 South Korean television series starring Park Bo-young, Ahn Hyo-seop and Lee Sung-jae",
        "The Bride of Habaek is a South Korean television drama spin-off of the 2006 sunjung manhwa Bride of the Water God by Yoon Mi-kyung.",
        "Her Private Life is a 2019 South Korean television series, created by Kim Hye-young and directed by Hong Jong-chan, starring Park Min-young, Kim Jae-wook, and Ahn Bo-hyun",
        "A love story between Captain Yoo Shi Jin, Korean Special Forces, and Doctor Kang Mo Yeon, surgeon at Haesung Hospital. Together they face danger in a war-torn country",
        "A woman born with superhuman strength is hired by the CEO of a gaming company, to be his bodyguard",
        "My Fellow Citizens! is a 2019 South Korean television series starring Choi Si-won, Lee Yoo-young and Kim Min-jung. It aired from April 1 to May 28, 2019 on KBS2",
        "My Fellow Citizens! is a 2019 South Korean television series starring Choi Si-won, Lee Yoo-young and Kim Min-jung. It aired from April 1 to May 28, 2019 on KBS2"

    };

    // ARRAY RECORD IMAGE
    private static int[] record_imager = {
        R.drawable.encounter,
        R.drawable.whatswrong,
        R.drawable.memories,
        R.drawable.skycastle,
        R.drawable.hotel,
        R.drawable.abyss,
        R.drawable.habaek,
        R.drawable.private_life,
        R.drawable.dots,
        R.drawable.dong,
        R.drawable.fellow,
            R.drawable.fellow,
    };

    static ArrayList<RecordData> getListData() {

        // INITIALIZE ARRAY
        ArrayList<RecordData> list = new ArrayList<>();

        // LOOP THROUGH ARRAY AND RECONSTRUCT
        for(int position = 0; position < record_id.length; position++){
            RecordData record = new RecordData(position,record_id[position],record_data[position],record_imager[position]);
            list.add(record);
        }

        // RETURN ALL THE ARRAY
        return list;
    }

    // GET ID
    static String getId(int pos){
        String id = record_id[pos];
        return id;
    }

    // GET DATA
    static String getData(int pos){
        String data = record_data[pos];
        return data;
    }

    // GET IMAGE
    static int getImage(int pos){
        int imago = record_imager[pos];
        return imago;
    }

}
