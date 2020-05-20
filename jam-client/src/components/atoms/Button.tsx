import React, { useState } from 'react';
import styled from '@emotion/styled';

interface Props {
  iconColor?: boolean;
  onClick?: () => void;
}

interface IconTextProps {
  iconName?: string;
  hasBorder?: boolean;
}

export const CommonButtonStyled = {
  background: 'transparent',
  border: 'transparent',
  padding: 0,
  cursor: 'pointer',
};

export const IconButtonStyled = styled.button(
  CommonButtonStyled,
  {
    minWidth: 0,
    width: 40,
    height: 40,
    flexShrink: 0,
    borderRadius: '50%',
  },
  (props: { iconColor?: boolean }) => ({
    color: props.iconColor ? '#ff0000' : 'inherit',
  })
);

export const IconButton: React.FC<Props> = ({
  iconColor,
  onClick,
  children,
}) => {
  return (
    <IconButtonStyled onClick={onClick} iconColor={iconColor}>
      <i className="material-icons">{children}</i>
    </IconButtonStyled>
  );
};

const IconTextButtonStyled = styled.button(
  CommonButtonStyled,
  {
    padding: '4px 8px',
    display: 'flex',
    alignItems: 'center',
    borderRadius: 4,
  },
  (props: { hasBorder?: boolean }) => ({
    border: props.hasBorder ? '1px solid #ddd' : 'none',
  })
);

const IconTextButtonLabel = styled.span({
  marginLeft: 4,
});

export const IconTextButton: React.FC<IconTextProps> = ({
  iconName,
  hasBorder,
  children,
}) => {
  return (
    <IconTextButtonStyled hasBorder={hasBorder}>
      <i className="material-icons">{iconName}</i>
      <IconTextButtonLabel>{children}</IconTextButtonLabel>
    </IconTextButtonStyled>
  );
};
