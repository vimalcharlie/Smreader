
 To find the device harwire settings

  String str[]=new String[40];
        String str1[]=new String[40];
        str[0]= Build.MANUFACTURER;
        str[1] = Build.MODEL;
        str[2]= Build.BRAND;
        str[3]= Build.HARDWARE;
        str[4]= Build.DEVICE;
        str[5]= Build.BOARD;
        str[6]= Build.ID;
        str[7]= Build.BOARD;
        str[8]= Build.PRODUCT;

        str1[0]= "MANUFACTURER";
        str1[1] = "MODEL";
        str1[2]= "BRAND";
        str1[3]= "HARDWARE";
        str1[4]= "DEVICE";
        str1[5]= "BOARD";
        str1[6]= "ID";
        str1[7]= "BOARD";
        str1[8]= "PRODUCT";

        for (int i = 0; i < 9; i++) {
            Log.e(getLocalClassName()+i,str1[i]+" : "+str[i]);
        }




        editText2.setText(str[0]);