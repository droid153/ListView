package com.example.matrix.listview;

/**
 * Created by matrix on 10/4/2014.
 */
public class Deals {
//    private String mDealType;
    private String mMerchantName;
    private String mShortTitle;
//    private String mOfferStartDate;
//    private String mOfferEndDate;

    public void setMerchantName(String merchantName){ mMerchantName = merchantName; }
    public void setShortTitle(String shortTitle){ mShortTitle = shortTitle; }
//    public void setDealType(String dealType){ mDealType = dealType; }
//    public void setOfferStartDate(String dealType){ mOfferStartDate = dealType; }
//    public void setOfferEndDate(String offerEndDate){ mOfferEndDate = offerEndDate; }


    public String getMerchantName(){ return mMerchantName; }
    public String getShortTitle(){ return mShortTitle; }
//    public String getDealType(){ return mDealType; }
//    public String getOfferStartDate(){ return mOfferStartDate; }
//    public String getOfferEndDate(){ return mOfferEndDate; }

}
