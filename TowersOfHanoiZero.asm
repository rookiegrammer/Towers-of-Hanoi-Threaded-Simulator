global Java_cmsc142mp01_JNITowersOfHanoi_towersOfHanoiZero

section .data

section .text

Java_cmsc142mp01_JNITowersOfHanoi_towersOfHanoiZero:
  cmp r8, 1
  je proc_tohz_end

  sub r8, 1
  push r8
  call Java_cmsc142mp01_JNITowersOfHanoi_towersOfHanoiZero
  pop r8
  call Java_cmsc142mp01_JNITowersOfHanoi_towersOfHanoiZero

proc_tohz_end:
  ret
