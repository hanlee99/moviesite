// app.js
import { initBoxoffice } from './sections/boxoffice.js';
//import { initShowtime } from './sections/showtime.js';
import { initCinema } from './sections/cinema.js';

document.addEventListener('DOMContentLoaded', () => {
  initBoxoffice(); // 첫 섹션 즉시 실행
  initCinema();

  let showtimeInit = false;
  let cinemaInit = false;

  /*window.addEventListener('scroll', () => {
    const scrollY = window.scrollY;
    const vh = window.innerHeight;

    if (scrollY > vh * 0.8 && !showtimeInit) {
      initShowtime();
      showtimeInit = true;
    }
    if (scrollY > vh * 1.8 && !cinemaInit) {
      initCinema();
      cinemaInit = true;
    }
  });*/
});
