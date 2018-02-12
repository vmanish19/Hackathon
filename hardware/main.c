#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <wiringPi.h>
#include <ads1115.h>
#include <pthread.h>
#include <errno.h>

#include "server/server.h"

#define MY_BASE 2222

#define JOG_BUT 4
#define RED_BUT 17
#define YELLOW_BUT 27
#define GREEN_BUT 22
#define JOG_DATA_A 20
#define JOG_DATA_B 16
#define SLIDER 0
#define POT_KNOB 2

extern server_t* g_server;

void socketCallback( int type, const char* value) {
  // To declare variables inside case you need to enable a scope with { }

  // printf("DEBUG - Data called back of %d : %s\n", type, value);
  switch(type) {

  case 0:
      break;

  case 1: {
    break;
  }
  case 2:
     break;

  default:
    printf("Not a valid type! [%d]\n", type);
    break;
  }
}

static unsigned long last_red_irq = 0;
static unsigned long last_yellow_irq = 0;
static unsigned long last_green_irq = 0;
static unsigned long last_jog_irq = 0;
static unsigned long c_red_irq = 0;
static unsigned long c_yellow_irq = 0;
static unsigned long c_green_irq = 0;
static unsigned long c_jog_irq = 0;
static int last_slider_value = 0;

void redButton(void) {
  c_red_irq = millis();
  if (c_red_irq - last_red_irq > 150) {
      puts("red");
      broadcastInt("0", 0);
  }
  last_red_irq = c_red_irq;
}
void yellowButton(void) {
  c_yellow_irq = millis();
  if (c_yellow_irq - last_yellow_irq > 150) {
      puts("yellow");
      broadcastInt("1", 0);
  }
  last_yellow_irq = c_yellow_irq;
}
void greenButton(void) {
  c_green_irq = millis();
  // bad circuit?
  if ((c_green_irq - last_green_irq > 150) && (c_green_irq - last_yellow_irq > 350)) {
      puts("green");
      broadcastInt("2", 0);
  }
  last_green_irq = c_green_irq;
}
void jogButton(void) {
  c_jog_irq = millis();
  if (c_jog_irq - last_jog_irq > 150) {
      puts("jog");
      broadcastInt("3", 0);
  }
  last_jog_irq = c_jog_irq;
}

int main ( int argc, char* argv[] ) {

  int slider_v;
  char jog_flag;
  char jog_last_status;
  char jog_cur_status;
  int jog_count = 0; // left is neg, right is pos
  
  g_server = (server_t*)malloc(sizeof(server_t));
  g_server->port = 6419;
  g_server->onSocketMessage = socketCallback;

  startServer();

  // sets up the wiringPi library
  if (wiringPiSetupGpio () < 0) {
    fprintf (stderr, "Unable to setup wiringPi: %s\n", strerror (errno));  return 1;
  }

  if ( wiringPiISR (RED_BUT, INT_EDGE_RISING, &redButton) < 0 ) {
    fprintf (stderr, "Unable to setup red ISR: %s\n", strerror (errno)); return 1;
  }
  if ( wiringPiISR (YELLOW_BUT, INT_EDGE_RISING, &yellowButton) < 0 ) {
    fprintf (stderr, "Unable to setup yellow ISR: %s\n", strerror (errno)); return 1;
  }
  if ( wiringPiISR (GREEN_BUT, INT_EDGE_RISING, &greenButton) < 0 ) {
    fprintf (stderr, "Unable to setup green ISR: %s\n", strerror (errno)); return 1;
  }
  if ( wiringPiISR (JOG_BUT, INT_EDGE_FALLING, &jogButton) < 0 ) {
    fprintf (stderr, "Unable to setup jog ISR: %s\n", strerror (errno)); return 1;
  }

  pinMode(JOG_DATA_A, INPUT);
  pinMode(JOG_DATA_B, INPUT);

  jog_last_status = digitalRead(JOG_DATA_B);
  
  ads1115Setup (MY_BASE, 0x48);
  
  // main infinite loop
  while(1) {

    // TODO - gets blocked until first jog spin
    slider_v = analogRead (MY_BASE + SLIDER);
    if (abs(slider_v - last_slider_value) > 300) {
      printf("Slider Value: %d\n", slider_v / 732);
      broadcastInt("6", slider_v / 732);
      last_slider_value = slider_v;
    }

    jog_last_status = digitalRead(JOG_DATA_B);

    while (!digitalRead(JOG_DATA_A)) {
      jog_cur_status = digitalRead(JOG_DATA_B);
      jog_flag = 1;
    }

    if (jog_flag == 1) {
      jog_flag = 0;
      if ((jog_last_status == 0) && (jog_cur_status == 1)) {
	if (jog_count < 0) { jog_count = 1; }
	else if (jog_count > 2) {
	  puts("RIGHT");
	  broadcastInt("5", 0);
	  jog_count = 0;
	}
	else { jog_count++; }
      }
      if ((jog_last_status == 1) && (jog_cur_status == 0)) {
	if (jog_count > 0) { jog_count = -1; }
	else if (jog_count < -2) {
	  puts("LEFT");
	  broadcastInt("4", 0);
	  jog_count = 0;
	}
	else { jog_count--; }
      }
    }
    //    usleep(100000); // .1 sec
  }
}
