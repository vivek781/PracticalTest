package com.example.practicaltest;

import java.util.ArrayList;

public class AllRecords
{
   String id,country_id,state_id,city_id,status,is_block;
   String profile_pic,business_name,person_name,email,country,state,city,address,contact,zip_code,about_us,business_images;
   Double c_latitude,c_longitude,distance_in_km,distance_in_meter;
   double distance_in_miles;
   String total_review,total_party_joined,total_party_animal,is_busiest_location,is_event_nearest_me,
           join_party_event,is_favorite,is_public_profile;
   float avg_rating;
   String rating,review,type;

   public ArrayList<record_img> array_images;
   public ArrayList<rating_review> array_rating;

   public void AllRecords(){
      id = "";
      profile_pic = "";
      person_name = "";
      email = "";
      country = "";
      state = "";
      city = "";
      address = "";
      contact = "";
      zip_code = "";
      about_us = "";
      business_images = "";
      profile_pic = "";
      profile_pic = "";
      country_id = "";
      business_name = "";
      state_id = "";
      city_id = "";
      status = "";
      is_block = "";
      total_review = "";
      total_party_joined = "";
      total_party_animal = "";
      is_busiest_location = "";
      is_event_nearest_me = "";
      join_party_event = "";
      is_favorite = "";
      is_public_profile = "";
      total_party_joined = "";
      total_party_joined = "";
      total_party_joined = "";
      rating = "";
      review = "";
      type = "";
      avg_rating = 0;
      c_latitude = 0.0;
      c_longitude = 0.0;
      distance_in_km = 0.0;
      distance_in_miles = 0.0;
      distance_in_meter = 0.0;
   }

   public ArrayList<record_img> getArray_images() {
      return array_images;
   }

   public void setArray_images(ArrayList<record_img> array_images)
   {
      this.array_images = array_images;
   }

   public ArrayList<rating_review> getArray_rating()
   {
      return array_rating;
   }

   public void setArray_rating(ArrayList<rating_review> array_rating)
   {
      this.array_rating = array_rating;
   }
}
