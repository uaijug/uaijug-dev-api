package br.com.uaijug.uaijugdevapi.model.service.impl;

import br.com.uaijug.uaijugdevapi.model.domain.Associate;
import br.com.uaijug.uaijugdevapi.model.service.AssociateService;
import br.com.uaijug.uaijugdevapi.model.service.DrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class DrawServiceImpl implements DrawService {

    @Autowired
    private AssociateService associateService;

    Long[] items = new Long[]{
            425710L,
            310968L,
            94613L,
            32146L,
            281076L,
            896105L,
            53916L,
            743102L,
            47523L,
            821013L,
            958110L,
            28491L,
            67589L,
            78592L,
            89721L,
            95674L,
            478910L,
            54983L,
            56317L,
            281091L,
            29754L,
            64891L,
            821074L,
            271093L,
            46752L,
            510624L,
            810394L,
            110347L,
            78465L,
            42156L,
            19863L,
            871049L,
            81462L,
            42768L,
            310598L,
            56297L,
            23678L,
            381052L,
            64598L,
            241710L,
            104617L,
            42369L,
            731065L,
            42987L,
            910587L,
            83967L,
            869387736L,
            313707834L,
            48552874L,
            3832508947L,
            849326268L,
            977048350L,
            4385945774L,
            578726638L,
            5116985773L,
            6228256946L,
            472504331L,
            2914499134L,
            63679258L,
            2125423919L,
            873545752L,
            284618967L,
            616819742L,
            4721966848L,
            743991912L,
            9064221670L,
            1097596356L,
            50581445L,
            435681771L,
            9849995327L,
            4146211842L,
            8154857697L,
            178693246L,
            9159859773L,
            6943475871L,
            4080526250L,
            10043828042L,
            672129912L,
            6923338036L,
            814254897L,
            298684525L,
            8624692191L,
            8350103514L,
            25386492L,
            9445965068L,
            7852227462L,
            4953962385L,
            48177594100L,
            276962299L,
            1264598933L,
            10010463226L,
            4181968344L,
            2351287243L};

    @Override
    public Set<Associate> prizeDrawing(int total) {
        Set<Associate> peopleDrawn = new HashSet<>();

        List<Associate> list = associateService.list();
        //Collections.shuffle(list);
       /* List<Long> longs = new Random().longs(5)
                .boxed()
                .collect(Collectors.toList()); */

        if (list.size() >= total) {
            for (var i = 1; i <= total; i++) {
                Associate associate = list.get(getRandom(list.size()));
                String code = associate.getCode();
                log.info("teste->" + code);
                peopleDrawn.add(associate);
            }
        }
        return peopleDrawn;
    }

    /*     new Random()
        .ints(amount, 1, 7).boxed()
        .collect(Collectors.groupingBy(s -> s))
        .forEach((k, v) -> System.out.println(k + ": "+v.size()));;*/

    private static int getRandom(int itensLegth) {
        return new Random().nextInt(itensLegth);
    }
}


