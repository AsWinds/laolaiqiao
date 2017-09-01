package com.codi.laolaiqiao.common.sensitiveword;

import java.util.Set;

public class Main {

	public static void main(String[] args) {
		String text = "潮喷我艹这傻逼江泽民忽闻腐败法轮李洪志大煞笔操你妈这个屌大傻吊, 这只是一遍测试温州帮大家千万不要找我茬哈哈哈html也熬过滤过滤http://baidu.com李愚蠢中国猪台湾猪进化不完全的生命体震死他们贱人装b大sb傻逼傻b煞逼煞笔刹笔傻比沙比欠干婊子养的我日你我操我草卧艹卧槽爆你菊艹你cao你你他妈真他妈别他吗草你吗草你丫操你妈擦你妈操你娘操他妈日你妈干你妈干你娘娘西皮狗操狗草狗杂种狗日的操你祖宗操你全家操你大爷妈逼你麻痹麻痹的妈了个逼马勒狗娘养贱比贱b下贱死全家全家死光全家不得好死全家死绝白痴无耻sb杀b你吗b你妈的婊子贱货人渣混蛋媚外和弦兼职限量铃声性伴侣男公关火辣精子射精诱奸强奸做爱性爱发生关系按摩快感处男猛男少妇屌屁股下体a片内裤浑圆咪咪发情刺激白嫩粉嫩兽性风骚呻吟sm阉割高潮裸露不穿一丝不挂脱光干你干死我干中日没有不友好的木牛流马的污染比汽车飞机大他们嫌我挡了城市的道路当官靠后台警察我们是为人民服务的中石化说亏损做人不能太cctv了领导干部吃王八工商税务两条狼公检法是流氓公安把秩序搞乱剖腹一刀五千几读不起选个学校三万起父母下岗儿下地裙中性运动";
		SensitivewordFilter filter = SensitivewordFilter.getInstance();
		
		Set<String> set = filter.getSensitiveWord(text, 1); 
		System.out.println(set);
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
	        filter.isContaintSensitiveWord(text, SensitivewordFilter.minMatchTYpe);
        }
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		
		start = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
	        filter.replaceSensitiveWord(text, SensitivewordFilter.minMatchTYpe, "*");
        }
		end = System.currentTimeMillis();
		System.out.println(end - start);
		
		System.out.println(filter.isContaintSensitiveWord(text, SensitivewordFilter.maxMatchType));
		System.out.println(filter.replaceSensitiveWord(text, SensitivewordFilter.maxMatchType, "*"));
		
	}

}
