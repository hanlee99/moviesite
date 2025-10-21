document.addEventListener("DOMContentLoaded", () => {
  const sections = document.querySelectorAll("section[id]");

  // ✅ 스크롤 감지: 현재 보이는 섹션 기준으로 주소만 갱신
 /* const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        const id = entry.target.id;
        const newUrl = id === "boxoffice" ? "/" : `/${id}`;
        if (window.location.pathname !== newUrl) {
          history.pushState({}, "", newUrl);
        }
      }
    });
  }, { threshold: 0.6 }); // 60% 이상 보일 때만 감지

  sections.forEach(sec => observer.observe(sec));

  // ✅ 뒤로가기/앞으로가기 시 스크롤 이동
  window.addEventListener("popstate", () => {
    const path = window.location.pathname.replace("/", "") || "boxoffice";
    const target = document.getElementById(path);
    if (target) {
      target.scrollIntoView({ behavior: "smooth" });
    }
  });*/
});
