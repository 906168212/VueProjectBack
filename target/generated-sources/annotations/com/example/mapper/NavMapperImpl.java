package com.example.mapper;

import com.example.Entity.dto.Account;
import com.example.Entity.dto.AccountDetails;
import com.example.Entity.dto.AccountDetailsDTO;
import com.example.Entity.dto.LevelDTO;
import com.example.Entity.dto.LevelInfo;
import com.example.Entity.dto.NavDTO;
import com.example.Entity.dto.VipDTO;
import com.example.Entity.dto.VipInfo;
import com.example.Entity.dto.Wallet;
import com.example.Entity.dto.WalletDTO;
import com.example.Entity.vo.response.LevelVO;
import com.example.Entity.vo.response.NavVO;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-14T19:06:41+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class NavMapperImpl implements NavMapper {

    @Autowired
    private CustomMapper customMapper;

    @Override
    public NavDTO toNavDTO(Account account) {
        if ( account == null ) {
            return null;
        }

        NavDTO navDTO = new NavDTO();

        navDTO.setUid( account.getSid() );
        navDTO.setDetails( accountDetailsToAccountDetailsDTO( account.getAccountDetails() ) );
        navDTO.setVip( vipInfoToVipDTO( account.getVipInfo() ) );
        navDTO.setLevel( levelInfoToLevelDTO( account.getLevelInfo() ) );
        navDTO.setUsername( account.getUsername() );
        navDTO.setNickname( account.getNickname() );
        navDTO.setCreateTime( customMapper.mapTimestampToLong( account.getCreateTime() ) );
        navDTO.setWallet( walletToWalletDTO( account.getWallet() ) );

        return navDTO;
    }

    @Override
    public NavVO toNavVO(NavDTO navDTO) {
        if ( navDTO == null ) {
            return null;
        }

        NavVO navVO = new NavVO();

        try {
            navVO.setLevel( levelDTOToLevelVO( navDTO.getLevel() ) );
        }
        catch ( Exception e ) {
            throw new RuntimeException( e );
        }
        navVO.setUid( navDTO.getUid() );
        navVO.setUsername( navDTO.getUsername() );
        navVO.setNickname( navDTO.getNickname() );
        navVO.setCreateTime( navDTO.getCreateTime() );
        navVO.setWallet( navDTO.getWallet() );
        navVO.setDetails( navDTO.getDetails() );
        navVO.setVip( navDTO.getVip() );

        return navVO;
    }

    protected AccountDetailsDTO accountDetailsToAccountDetailsDTO(AccountDetails accountDetails) {
        if ( accountDetails == null ) {
            return null;
        }

        AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO();

        accountDetailsDTO.setConcernedNum( accountDetails.getConcernedNum() );
        accountDetailsDTO.setMotionNum( accountDetails.getMotionNum() );
        accountDetailsDTO.setFanNum( accountDetails.getFanNum() );

        return accountDetailsDTO;
    }

    protected VipDTO vipInfoToVipDTO(VipInfo vipInfo) {
        if ( vipInfo == null ) {
            return null;
        }

        VipDTO vipDTO = new VipDTO();

        vipDTO.setStatus( vipInfo.getStatus() );
        vipDTO.setType( vipInfo.getType() );
        vipDTO.setOpeningTime( vipInfo.getOpeningTime() );
        vipDTO.setExpireTime( vipInfo.getExpireTime() );

        return vipDTO;
    }

    protected LevelDTO levelInfoToLevelDTO(LevelInfo levelInfo) {
        if ( levelInfo == null ) {
            return null;
        }

        LevelDTO levelDTO = new LevelDTO();

        levelDTO.setCurrentExp( levelInfo.getCurrentExp() );
        levelDTO.setCurrentLevel( levelInfo.getCurrentLevel() );
        levelDTO.setNextExp( levelInfo.getNextExp() );

        return levelDTO;
    }

    protected WalletDTO walletToWalletDTO(Wallet wallet) {
        if ( wallet == null ) {
            return null;
        }

        WalletDTO walletDTO = new WalletDTO();

        walletDTO.setFutureCoin( wallet.getFutureCoin() );
        walletDTO.setPointCoin( wallet.getPointCoin() );

        return walletDTO;
    }

    protected LevelVO levelDTOToLevelVO(LevelDTO levelDTO) throws Exception {
        if ( levelDTO == null ) {
            return null;
        }

        LevelVO levelVO = new LevelVO();

        levelVO.setCurrentLevelName( customMapper.mapLevelToName( levelDTO.getCurrentLevel() ) );
        levelVO.setCurrentExp( levelDTO.getCurrentExp() );
        levelVO.setCurrentLevel( levelDTO.getCurrentLevel() );
        levelVO.setNextExp( levelDTO.getNextExp() );

        return levelVO;
    }
}
