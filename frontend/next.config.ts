import type { NextConfig } from "next";

// 이미지 테스트용 무료 호스팅 사이트 활용
/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    domains: ['i.postimg.cc'],  // postimg.cc의 실제 이미지 서버 도메인
  },
}

export default nextConfig;
