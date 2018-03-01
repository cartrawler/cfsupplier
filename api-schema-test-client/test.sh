#!/bin/sh
VAR1=$1
VAR2=$2  

case $VAR1 in
   0)
    	ruby dependency_checker.rb
    	;;	
   1)
       ruby test.rb 1
      ;;
   2)
      ruby test.rb 2
      ;;
   3)
      ruby test.rb 3  $VAR2
      ;;
   4)
      ruby test.rb 4  $VAR2 
      ;;
   5)
      ruby test.rb 5   
      ;;
   6)
      ruby test.rb 6  $VAR2 
      ;;
esac