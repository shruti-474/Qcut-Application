<?php
require_once("databaseconnect.php");

$sql="select * from category";
$data=array();


$result = mysqli_query($connection,$sql);
while ($row=mysqli_fetch_array($result)){
array_push($data,array('id'=>$row[0],'categoryName'=>$row[1],'categoryImage'=>$row[2]));
}

echo json_encode(array('getHomecategory'=>$data));

?>