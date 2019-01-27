global Java_cmsc142mp01_JNITowersOfHanoi_towersOfHanoi

section .data

section .text

Java_cmsc142mp01_JNITowersOfHanoi_towersOfHanoi
    ; WINDOWS VER
    ; For other os: r8 -> rdx; r9 -> rcx; r10 -> r8; r11 -> r9

    mov r9, 1
    mov r10, 2
    mov r11, 3
    mov rax, r8
    call proc_toh

    ret

proc_toh:

    ; check if end case, otherwise jump
    cmp r8, 1
    jne proc_toh_n_plus

    ; print MOVE DISC 1 S->T, R9 is the source peg S, R11 is the target peg T
    ret

proc_toh_n_plus:
    ; save vars
    push rax
    push r9
    push r10
    push r11

    ; swap original aux pole with to pole addresses
    mov rax, r10   ; save r10
    mov r10, r11   ; move r11 to r10
    mov r11, rax  ; move saved r10 to r11

    mov rax, r8;
    sub r8, 1

    call proc_toh ; do toh(from, to, aux)

    ; print MOVE DISC N S->T, RAX is the disc number N, R9 is the source peg S, R11 is the target peg T
    ; we can now modify RAX :)

    mov r8, rax;
    sub r8, 1

    ; rotate pole addresses
    ; change (from, to, aux) to (aux, from, to)
    mov rax, r9 ;
    mov r9, r11 ;
    mov r11, r10;
    mov r10, rax;

    call proc_toh ; do toh(aux, from, to)

    ; retrieve vars
    pop r11
    pop r10
    pop r9
    pop rax
    ret
