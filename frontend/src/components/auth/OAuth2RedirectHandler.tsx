'use client';
import { useEffect } from 'react';
import { useRouter } from 'next/navigation';

interface OAuth2RedirectProps {
  onLoginSuccess?: (tokens: { accessToken: string }) => void;
}

const OAuth2RedirectHandler = ({ onLoginSuccess }: OAuth2RedirectProps) => {
  const router = useRouter();

  useEffect(() => {
    const handleOAuth2Redirect = async () => {
      const searchParams = new URLSearchParams(window.location.search);
      const status = searchParams.get('status');
      
      try {
        switch (status) {
          case 'REGISTER': {
            // 신규 OAuth 로그인인 경우
            const provider = searchParams.get('provider');
            const oauthId = searchParams.get('oauthId');
            
            console.log('OAuth2RedirectHandler - REGISTER:', { provider, oauthId });  // 디버깅 로그
            
            if (!provider || !oauthId) {
                throw new Error('OAuth 정보가 누락되었습니다.');
            }
            
            // 추가정보 입력 페이지로 이동
            router.push(`/join?provider=${encodeURIComponent(provider)}&oauthId=${encodeURIComponent(oauthId)}`);
            break;
          }
          
          case 'SUCCESS': {
            // OAuth 정보로 이미 가입된 회원인 경우
            const accessToken = searchParams.get('accessToken');
            
            if (!accessToken) {
              throw new Error('인증 토큰이 누락되었습니다.');
            }
            
            localStorage.setItem('accessToken', accessToken);
            onLoginSuccess?.({ accessToken });
            router.push('/');
            break;
          }
          
          default:
            throw new Error('잘못된 인증 상태입니다.');
        }
      } catch (error) {
        console.error('OAuth2 리다이렉트 처리 중 오류 발생:', error);
        
        // 에러 페이지 또는 로그인 페이지로 이동
        router.push(`/login?error=${encodeURIComponent(error instanceof Error ? error.message : '인증 처리 중 오류가 발생했습니다.')}`);
      }
    };

    handleOAuth2Redirect();
  }, [router, onLoginSuccess]);

  // 로딩 상태 표시
  return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-gray-900" />
    </div>
  );
};

export default OAuth2RedirectHandler;