import request from '@/utils/request'

export function createPayment(data) {
  return request({
    url: '/payment/create',
    method: 'post',
    data
  })
}

export function queryPaymentStatus(orderNo) {
  return request({
    url: `/payment/status/${orderNo}`,
    method: 'get'
  })
}

// 支付宝支付
export function alipay(orderNo) {
  return request({
    url: '/payment/alipay',
    method: 'post',
    data: { orderNo },
    responseType: 'blob'
  }).then(response => {
    const url = window.URL.createObjectURL(response)
    window.open(url, '_blank')
  })
}

// 微信支付
export function wechatPay(orderNo) {
  return request({
    url: '/payment/wechat',
    method: 'post',
    data: { orderNo }
  })
} 