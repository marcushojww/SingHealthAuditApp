import React from "react";

export function getDateString(date) {
  var year = date.getFullYear();
  var month = addZero(date.getMonth()+1);
  var day = addZero(date.getDate());
  return day + "/" + month + "/" + year;
};

export function getDateStringAfterOneMonth() {
  var today = new Date();
  var year = today.getFullYear();
  var month = parseInt(addZero(today.getMonth()+2));
  var day = addZero(today.getDate());
  if (month>12) {
    month -= 12;
    year += 1;
  }
  return day + "/" + month + "/" + year;
};

export function addZero(number) {
  if (number < 10) return "0" + number;
  else return number;
};

export function getTimeString() {
  var today = new Date();
  var hours = addZero(today.getHours());
  var minutes = addZero(today.getMinutes());
  var seconds = addZero(today.getSeconds());
  return hours + ":" + minutes + ":" + seconds;
};

// Raw String => Valid String
export function toValidFormat(raw_time_string) {
  // YYYY-MM-DD
  if (raw_time_string[4] === "-") {
    var string_array = raw_time_string.split("-");
    var year = string_array[0];
    var month = string_array[1];
    var day = string_array[2];
  } 
  // DD/MM/YYYY
  else if (raw_time_string[2] === "/") {
    var string_array = raw_time_string.split("/");
    var year = string_array[2];
    var month = string_array[1];
    var day = string_array[0];
  }
  return day + "/" + month + "/" + year;
}

// Date object => Valid String
export function dateToValidString(dateObject) {
  
}